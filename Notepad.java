import java.io.*;
import java.util.*;

public class Notepad extends DataSourceFile {

    private Deque<Pair> undoStack = new LinkedList<>();
    private Deque<Pair> redoStack = new LinkedList<>();

    ArrayList<String> copiedData = new ArrayList<>();

    private class Pair {
        String type;
        Object content;
        int line;

        Pair(String type, Object content, int line) {
            this.type = type;
            this.content = content;
            this.line = line;
        }
    }

    public Notepad(String fileName) throws IOException {
        super(fileName);
        System.out.println(super.checkCreateFile());
    }

    public String display()
    {
        return super.readFile();
    }

    public Collection<? extends String> display(int start, int end) {
        if (this.file.exists()) {
            List<String> lines = new ArrayList<>();
            try (Scanner reader = new Scanner(this.file)) {
                while (reader.hasNextLine()) {
                    lines.add(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found.");
                e.printStackTrace();
                return null;
            }

            if (start > end || start < 1 || end > lines.size()) {
                System.out.println("Invalid range for copying.");
                return null;
            }

            ArrayList<String> copiedContent = new ArrayList<>();
            for (int i = start - 1; i < end; i++) {
                copiedContent.add(lines.get(i));
            }
            return copiedContent;
        } else {
            System.out.println("The file does not exist.");
            return null;
        }
    }

    public void insert(int start, Object data) {
        if (this.file.exists()) {
            List<String> lines = new ArrayList<>();
            try (Scanner reader = new Scanner(this.file)) {
                while (reader.hasNextLine()) {
                    lines.add(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found.");
                e.printStackTrace();
                return;
            }

            int insertIndex = Math.max(0, Math.min(start - 1, lines.size()));

            if (data instanceof String) {
                lines.add(insertIndex, (String) data);
                undoStack.push(new Pair("insert", (String) data, insertIndex + 1));
//                redoStack.clear();
            } else if (data instanceof List) {
                List<String> dataList = (List<String>) data;
                lines.addAll(insertIndex, dataList);
                // Push multiple actions to undoStack
                for (int i = 0; i < dataList.size(); i++) {
                    undoStack.push(new Pair("insert", dataList.get(i), insertIndex + i + 1));
                }
//                redoStack.clear();
            } else {
                System.out.println("Invalid data type for the given InsertType.");
                return;
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(this.file))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error: Unable to write to file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public void delete(int n) {
        if (this.file.exists()) {
            List<String> lines = new ArrayList<>();
            try (Scanner reader = new Scanner(this.file)) {

                while (reader.hasNextLine()) {
                    lines.add(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found.");
                e.printStackTrace();
                return;
            }

            int delIndex = Math.max(0, Math.min(n - 1, lines.size()));
            // deleting the nth line!
            String deletedLine = lines.remove(delIndex);
            System.out.println(deletedLine);
            undoStack.push(new Pair("delete", deletedLine, delIndex + 1));

            try (PrintWriter writer = new PrintWriter(new FileWriter(this.file))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error: Unable to write to file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }


    public void redo() {
        if (!redoStack.isEmpty()) {
            Pair lastUndo = redoStack.pop();
            undoStack.push(lastUndo);
            switch (lastUndo.type) {
                case "insert":
                    System.out.println("Inserting the data :"+lastUndo.content+" at the line :"+lastUndo.line);
                    insert(lastUndo.line, lastUndo.content);
                    break;
                case "delete":
                    System.out.printf("Deleting data from the line "+lastUndo.line);
                    delete(lastUndo.line);
                    break;
                case "paste":
                    System.out.println("pasting the data!");
                    paste(lastUndo.line);
                    break;
            }
        }
        else
        {
            System.out.println("Ntg to redo!");
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Pair lastAction = undoStack.pop();
            redoStack.push(lastAction);
            switch (lastAction.type) {
                case "insert":
                    System.out.println("Your last action was inserting the data, i do delete  at the line "+lastAction.line);
                    delete(lastAction.line);
                    break;
                case "delete":
                    System.out.println("Your last action was deleting the data, so i do insertion"+lastAction.content+" at the line "+lastAction.line);
                    insert(lastAction.line, lastAction.content);
                    break;
                case "paste":
                    System.out.println("Your last action was pasting the data , so i do delete at the line "+lastAction.line);
                    delete(lastAction.line);
                    break;
            }
        }
        else {
            System.out.println("Ntg to Undo!");
        }
    }

    public void copy(int s , int e)
    {
        copiedData.clear();
        copiedData.addAll(display(s,e));
        System.out.println(copiedData);
    }

    public void paste(int line) {
        if (this.file.exists() && !copiedData.isEmpty()) {
            List<String> lines = new ArrayList<>();
            try (Scanner reader = new Scanner(this.file)) {
                while (reader.hasNextLine()) {
                    lines.add(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found.");
                e.printStackTrace();
                return;
            }

            int pasteIndex = Math.max(0, Math.min(line - 1, lines.size()));

            if(copiedData.size()==1)
            {
                lines.add(pasteIndex, copiedData.get(0));
                undoStack.push(new Pair("paste", copiedData.get(0), pasteIndex));
            }
            else {
                lines.addAll(pasteIndex, copiedData);
            }
//                redoStack.clear();

            try (PrintWriter writer = new PrintWriter(new FileWriter(this.file))) {
                for (String lineContent : lines) {
                    writer.println(lineContent);
                }
            } catch (IOException e) {
                System.out.println("Error: Unable to write to file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }

}