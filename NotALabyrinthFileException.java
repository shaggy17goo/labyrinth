import java.io.IOException;

public class NotALabyrinthFileException extends IOException {
    public NotALabyrinthFileException(){
        System.out.println("The given file does not have a labyrinth to be loaded");

    }
    public NotALabyrinthFileException(String str) {super(str);}

    @Override
    public String getMessage() {
        return "The given file does not have a labyrinth to be loaded";
    }
}
