package duke;
import java.util.*;

public class TaskList {
    private List<Task> list;

    /*
     * Constructor for class TaskList.
     */
    protected TaskList() {
        this.list = new ArrayList<>();
    }

    /*
     * converts the contents of the list of tasks to String.
     * @return String containing all items in the list
     */
    @Override 
    public String toString(){
        String itemString = "";
        int index = 1;
        for (Task item : list) {
            itemString += String.valueOf(index) + ". " + item.toString() + "\n";
            index++;
        }
        return itemString;
    }

    /*
     * getter for task objects in the list. 
     * @return List<Task>
     */
    protected List<Task> getTaskList() {
        return this.list;
    }

    /*
     * sets the indicated task as completed.
     * @param number the order of the task
     * @param ui the UI Object that prints messages to the console
     */
    protected String markAsDone(int number, UI ui){
        Task item = list.get(number - 1);
        item.mark();
        
        list.set(number - 1, item);
        return ui.markedMsg(item);
    }

    /*
     * Deletes specified task from list.
     * @param number the order of the list to be removed
     * @param ui the UI object that prints to screen
     */
    protected String deleteFromList(int number, UI ui) {
        Task removed = list.remove(number - 1);
        return ui.rmvMsg(removed, list.size());
    }

    /*
     * Unmarks task from list given a specified number.
     * @param number the index of the task to be unmarked
     * @param ui the UI Object that prints to screen
     */
    protected String unmarkTask(int number, UI ui) {
        Task item = list.get(number - 1);
        item.unmark();        
        list.set(number - 1, item);
        return ui.unmarkedMsg(item);
    }

    protected void addTaskFromFile(Task newTask) {
        this.list.add(newTask);
    }

    protected String addToList(String item, UI ui){
        try {
            Task newTask = new Task(item);
            list.add(newTask);
            int listSize = list.size();
            return ui.addToListMsg(listSize, newTask);
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    protected String findTasksByKeyword(String keyword, UI ui) {
        TaskList keywordTaskList = new TaskList();
        for (Task task : this.list) {
            String taskDesc = task.description;
            if (taskDesc.contains(keyword)) {
                keywordTaskList.list.add(task);
            } 
        }
        return ui.printList(keywordTaskList);
    }

}
