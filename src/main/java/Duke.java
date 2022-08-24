import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Duke {
    private static final String indentation = "    ";
    private static final String horizontalLine = "____________________________________________________________";
    private static List<Task> list = new ArrayList<>();

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        commandHandler();
    }

    private static void encapsulateMessage(String message){
        String[] messages = message.split("\n");
        drawLine();
        for (String msg : messages){
            System.out.println(indentation + " " + msg);
        }
        drawLine();
    }

    private static void drawLine(){
        System.out.println(indentation + horizontalLine + "\n");
    }

    private static void greet(){
        String greeting = "Hello! I'm Duke \n" 
                        + "What can I do for you? \n" ;
        
        encapsulateMessage(greeting);
    }

    private static void echo(String command){
        encapsulateMessage(command);
    }

    private static void exit(){
        String bye = "Bye. Hope to see you again soon!";
        encapsulateMessage(bye);
    }

    private static void addToList(String item){
        try {
            Task newTask = new Task(item);
            list.add(newTask);
            int listSize = list.size();
            String addedMsg = "Got it. I've added this task: \n"
                                + newTask.toString()
                                + "\n Now you have %d tasks in the list.";
            addedMsg = String.format(addedMsg, listSize);
            echo(addedMsg);
        } catch (Exception e) {
            echo(e.getMessage());
        }

    }

    private static void taskListReader(){
        File taskFile = new File("./data/duke.txt");
        try (Scanner input = new Scanner(taskFile)) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] taskAttrs = line.split(" \\| " , -1);
                if (taskAttrs[0].equals("T")) {
                    Task newTask = new Task(taskAttrs[0].charAt(0), taskAttrs[1].equals("X"), taskAttrs[2]);
                    list.add(newTask);
                } else {
                    Task newTask = new Task(taskAttrs[0].charAt(0), taskAttrs[1].equals("X"), taskAttrs[2], taskAttrs[3], taskAttrs[4]);
                    list.add(newTask);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void taskListWriter() {
        File taskFile = new File("./data/duke.txt");
        String taskString = getTaskString();
        try {
            FileWriter fw = new FileWriter(taskFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(taskString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getTaskString() {
        String tasks = "";
        for (Task task : list){
            tasks += task.getTaskString() + "\n";
        }
        return tasks;
    }

    private static void deleteFromList(int number) {
        Task removed = list.remove(number);
        String rmvMsg = "Noted. I've removed this task: \n"
                        + removed.toString()
                        + "\n Now you have %d tasks in the list.";
        rmvMsg = String.format(rmvMsg, list.size());
        echo(rmvMsg);
    }

    private static void markAsDone(int number){
        Task item = list.get(number - 1);
        item.mark();
        String itemMessage = "Nice! I've marked this task as done: \n"
        + item.toString();
        encapsulateMessage(itemMessage);
        list.set(number - 1, item);
    }

    private static void unmarkTask(int number) {
        Task item = list.get(number - 1);
        item.unmark();
        String itemMessage = "OK, I've marked this task as not done yet: \n"
        + item.toString();
        encapsulateMessage(itemMessage);
        list.set(number - 1, item);

    }

    private static void printList(){
        String itemString = "";
        int index = 1;
        for (Task item : list) {
            itemString += String.valueOf(index) + ". " + item.toString() + "\n";
            index++;
        }
        encapsulateMessage(itemString);
    }


    /*
     * The main Command Handling function for the bot.
     * It first greets the user, then takes in commands and keeps echoing them
     * Until the user inputs bye.
     */
    private static void commandHandler() {
        greet();

        // Read file with tasks if it exists, else create a new one.
        File taskFile = new File("./data/duke.txt");
        if (taskFile.exists()) {
            taskListReader();
        } else {
            try {
                taskFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Scan for commands
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        
        while (!command.toLowerCase().equals("bye")) {
            if (command.equals("list")){
                printList();
            } else if (command.split(" ")[0].toLowerCase().equals("mark")) {
                int number = Integer.parseInt(command.split(" ")[1]);
                markAsDone(number);
            } else if (command.split(" ")[0].toLowerCase().equals("unmark")) {
                int number = Integer.parseInt(command.split(" ")[1]);
                unmarkTask(number);
            } else if (command.split(" ")[0].toLowerCase().equals("delete")) {
                int number = Integer.parseInt(command.split(" ")[1]);
                deleteFromList(number);
            } else {
                addToList(command);
            }

            command = sc.nextLine();

        }

        // Loop has been exited, meaning bye has been inputted
        // Save the tasks to duke.txt
        taskListWriter();
        
        // Bye message
        exit();
        sc.close();
    }
}
