import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Notepad notepad = new Notepad("hello");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Copy");
            System.out.println("4. Paste");
            System.out.println("5. Undo");
            System.out.println("6. Redo");
            System.out.println("7. Display");
            System.out.println("8.Display all");
            System.out.println("9. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter the line number to insert at:");
                    int insertLine = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the content to insert:");
                    String content = scanner.nextLine();
                    notepad.insert(insertLine, content);
                    break;

                case 2:
                    System.out.println("Enter the line number to delete:");
                    int deleteLine = scanner.nextInt();
                    notepad.delete(deleteLine);
                    break;

                case 3:
                    System.out.println("Enter the start line number to copy from:");
                    int startCopy = scanner.nextInt();
                    System.out.println("Enter the end line number to copy to:");
                    int endCopy = scanner.nextInt();
                    notepad.copy(startCopy, endCopy);
                    System.out.println("Data copied to the clipboard!");
                    break;

                case 4:
                    System.out.println("Enter the line number to paste at:");
                    int pasteLine = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter the content to paste:");
                    notepad.paste(pasteLine);
                    break;

                case 5:
                    notepad.undo();
                    break;

                case 6:
                    notepad.redo();
                    break;

                case 7:
                    System.out.println("Enter the start line number to display from:");
                    int startDisplay = scanner.nextInt();
                    System.out.println("Enter the end line number to display to:");
                    int endDisplay = scanner.nextInt();
                    Object out = notepad.display(startDisplay,endDisplay);
                    if(out instanceof String)
                    {
                        System.out.println((String) out);
                    }
                    else
                    {
                        System.out.println((ArrayList<String>) out);
                    }
                    break;

                case 8:
                    System.out.println(notepad.display());
                    break;

                case 9:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
