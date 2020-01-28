import java.io.IOException;

public class NotAProperFileNameException extends IOException {
        public NotAProperFileNameException(){
            System.out.println("The inputted filename is not valid");

        }
        public NotAProperFileNameException(String str) {super(str);}

    @Override
    public String getMessage() {
        return "The inputted filename is not valid";
    }
}

