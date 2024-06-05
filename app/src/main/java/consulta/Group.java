package consulta;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public String string;
    public int status;
    public final List<Children> children = new ArrayList<Children>();

    public Group(String string) {
        this.string = string;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status = status;
    }
}
