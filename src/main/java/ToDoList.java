import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Task> list;
    private Storage storage;

    /* Define constructor for to do list*/
    public ToDoList(ArrayList<Task> list, Storage storage) {
        this.list = list;
        this.storage = storage;
    }

    /* Method for adding items to the list */
    public void addTask(String command) throws BobException {
        if (command.startsWith("todo")) {
            command = command.replace("todo", "");
            if (command.length() == 0) {
                System.err.println(
                        "\n   --------------------------------------------------------------------------------\n" +
                                "     The description cannot be empty!\n" +
                                "     You can't possibly just do nothing... Right? Right? Guys I'm RIGHT right?\n" +
                                "   --------------------------------------------------------------------------------"
                );
            } else {
                Task todo = new Todo(command);
                list.add(todo);
                storage.store(list);

                System.out.println(
                        "   --------------------------------------------------------------------------------\n" +
                                "     Got it. I've added this task: \n" +
                                "       " + todo.toString() + "\n" +
                                "     You now have " + list.size() + (list.size() == 1 ? " task" : " tasks") + " in the list.\n" +
                                "   --------------------------------------------------------------------------------"
                );
            }
        } else if (command.startsWith("deadline")) {
            try {
                command = command.replace("deadline ", "");
                if (command.length() == 0) {
                    System.err.println(
                            "\n   --------------------------------------------------------------------------------\n" +
                                    "     The description of deadline cannot be empty!\n" +
                                    "   --------------------------------------------------------------------------------"
                    );
                } else {
                    String[] deadline = command.split(" /by ");

                    String by = deadline[1];

                    // generate boolean indicating if the deadline is before or after current date and time
                    LocalDateTime currDate = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd kkmm");
                    LocalDateTime deadlineDate = LocalDateTime.parse(by, format);
                    Boolean isAfter = deadlineDate.isAfter(currDate);

                    String regex = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2})(\\d{2})";
                    // invalid deadline format
                    if (!by.trim().matches(regex)) {
                        System.err.println(
                                "\n   --------------------------------------------------------------------------------\n" +
                                        "     Invalid formatting for deadline entered!\n" +
                                        "     Write your deadlines in the following format: YYYY-MM-DD 0000\n" +
                                        "   --------------------------------------------------------------------------------"
                        );
                    } else if (by.trim().matches(regex) && !isAfter) {
                        System.err.println(
                                "\n   --------------------------------------------------------------------------------\n" +
                                        "     I might be a non-sentient robot but you seem to be a time traveller!\n" +
                                        "     Please input deadlines BEFORE the current date and time.\n" +
                                        "   --------------------------------------------------------------------------------"
                        );
                    } else {
                        Task task = new Deadline(deadline[0], deadline[1]);
                        list.add(task);
                        storage.store(list);

                        System.out.println(
                                "   --------------------------------------------------------------------------------\n" +
                                        "     Got it. I've added this task: \n" +
                                        "       " + task.toString() + "\n" +
                                        "     You now have " + list.size() + (list.size() == 1 ? " task" : " tasks") + " in the list.\n" +
                                        "   --------------------------------------------------------------------------------"
                        );
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(
                        "\n   --------------------------------------------------------------------------------\n" +
                                "       Invalid input! Set a deadline for your task!\n" +
                                "       You need to finish your tasks eventually... Right? Right?\n" +
                                "   --------------------------------------------------------------------------------"
                );
            }
        } else if (command.startsWith("event")) {
            try {
                command = command.replace("event ", "");
                if (command.length() == 0) {
                    System.err.println(
                            "   --------------------------------------------------------------------------------\n" +
                                    "     The description of event cannot be empty!\n" +
                                    "     It's impossible to go for something that does not exist...\n" +
                                    "   --------------------------------------------------------------------------------"
                    );
                } else {
                    String[] event = command.split(" /at ");
                    Task task = new Event(event[0], event[1]);
                    list.add(task);
                    storage.store(list);

                    System.out.println(
                            "   --------------------------------------------------------------------------------\n" +
                                    "     Got it. I've added this task: \n" +
                                    "       " + task.toString() + "\n" +
                                    "     You now have " + list.size() + (list.size() == 1 ? " task" : " tasks") + " in the list.\n" +
                                    "   --------------------------------------------------------------------------------"
                    );
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(
                        "\n   --------------------------------------------------------------------------------\n" +
                                "       Please set a date for your event!\n" +
                                "   --------------------------------------------------------------------------------"
                );
            }
        } else {
            System.err.println(
                "\n   --------------------------------------------------------------------------------\n" +
                            "     Deepest apologies, I am a mere automated bot.\n" +
                            "     Please stick to input that start with \n" + 
                            "     1. todo - for items that you have to do\n" + 
                            "     2. deadline - for items which have an upcoming deadline\n" + 
                            "     3. event - for events with a date and time\n" +
                            "     \n" +
                            "     4. mark - to mark an event as done\n" + 
                            "     5. unmark - to mark an event as undone\n" + 
                            "     6. delete - to delete an event\n" + 
                            "     7. list - to view all the events on your todo list\n" +
                            "     8. bye - to wish me a (temporary) farewell\n" +
                            "   --------------------------------------------------------------------------------"
            );
        }
    }

    /* Method for deleting a specific event */
    public void deleteTask(int index) {
        System.out.println(
                 "   --------------------------------------------------------------------------------\n" +
                         "     Got it. I've removed this task: \n" +
                         "       " + this.list.get(index - 1).toString() + "\n" +
                         "     You now have " + (list.size() - 1) + (list.size() - 1 == 1 ? " task" : " tasks") + " in the list.\n" +
                         "   --------------------------------------------------------------------------------"
        );
        this.list.remove(index - 1);
        storage.store(list);
    }

    /* Method to mark a certain item in the list as done */
    public void markItemDone(int index) {
        this.list.get(index - 1).markDone();

        System.out.println(
                "   --------------------------------------------------------------------------------\n" +
                        "     GOOD JOB! I'm marking this task as done: \n" +
                        "       " + this.list.get(index - 1).toString() + "\n" +
                        "   --------------------------------------------------------------------------------"
        );
        storage.store(list);
    }

    /* Method to mark a certain item in the list as undone */
    public void markItemUndone(int index) {
        this.list.get(index - 1).markUndone();

        System.out.println(
                "   --------------------------------------------------------------------------------\n" +
                        "     GOOD JOB! But it would be even better if you got this done: \n" +
                        "       " + this.list.get(index - 1).toString() + "\n" +
                        "   --------------------------------------------------------------------------------"
        );
        storage.store(list);
    }

    /* Method for printing items in the list */
    @Override
    public String toString() {
        int numOfElements = this.list.size();
        String res = "   --------------------------------------------------------------------------------\n";
        res += "      Here are your tasks:\n";
        for (int i = 1; i <= numOfElements; i++) {
            String curr = "      " + i + ". " + this.list.get(i - 1).toString() + "\n";
            res += curr;
        }
        res += "   --------------------------------------------------------------------------------";
        return res;
    }
}
