package duke;

import java.io.IOException;

/**
 * Utility class to parse user input.
 */
public class Parser {

    private Storage storage;
    private TaskList tasks;

    /**
     * Parser contains the Storage, Tasks, and return duke.Ui classes and acts as an
     * intermediary for them to interact.
     * @param storage Storage class
     * @param tasks   TaskList class
     */
    public Parser(Storage storage, TaskList tasks) {
        this.storage = storage;
        this.tasks = tasks;
    }

    /**
     * Instantiates the scanner and parses inputs.
     */
    public String response(String input) {
        try {
            return identifyInput(input);
        } catch (DukeTaskException e1) {
            return duke.Ui.showDukeTaskError();
        } catch (IndexOutOfBoundsException e2) {
            return duke.Ui.showDukeEmptyListError();
        } catch (IOException e3) {
            return duke.Ui.showLoadingError();
        } catch (Exception e4) {
            return duke.Ui.showDukeGeneralError();
        }
    }

    private String identifyInput(String input) throws DukeGeneralException, DukeTaskException, IOException {
        Task temp;
        String output;
        switch (getCommand(input)) {
        case "bye":
            try {
                storage.save(tasks);
            } catch (IOException e) {
                return duke.Ui.showLoadingError();
            }
            output = duke.Ui.showBye();
            break;
        case "list":
            if (tasks.isEmpty()) {
                return duke.Ui.showDukeEmptyListError();
            }
            output = duke.Ui.showList(tasks);
            break;
        case "find":
            output = duke.Ui.showFind(tasks, getMessage(input).trim());
            break;
        case "done":
            tasks.setDone(duke.Ui.getIndex(input));
            output = duke.Ui.showDone(input, tasks);
            break;
        case "todo":
            temp = new ToDo(getMessage(input));
            output = duke.Ui.showTaskAdded(tasks, temp);
            tasks.add(temp);
            break;
        case "deadline":
            temp = new Deadline(getMessage(input));
            output = duke.Ui.showTaskAdded(tasks, temp);
            tasks.add(temp);
            break;
        case "event":
            temp = new Event(getMessage(input));
            output = duke.Ui.showTaskAdded(tasks, temp);
            tasks.add(temp);
            break;
        case "delete":
            output = duke.Ui.showDeleteTask(input, tasks);
            tasks.remove(duke.Ui.getIndex(input));
            break;
        case " ":
            output = duke.Ui.showEmptyError();
            break;
        case "archive":
            String archivePath = getMessage(input).trim();
            output = duke.Ui.showArchiveTasks(tasks, archivePath);
            storage.archive(tasks, archivePath);
            break;
        default:
            throw new DukeGeneralException("");
        }
        return output;
    }

    private String getCommand(String input) throws DukeTaskException {
        try {
            String[] inputArray = input.split(" ", 2);
            return inputArray[0].toLowerCase();
        } catch (Exception e) {
            throw new DukeTaskException("");
        }
    }

    private String getMessage(String input) throws DukeTaskException {
        try {
            String[] inputArray = input.split(" ", 2);
            return inputArray[1];
        } catch (Exception e) {
            throw new DukeTaskException("");
        }
    }

}
