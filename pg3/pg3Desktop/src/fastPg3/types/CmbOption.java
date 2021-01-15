package fastPg3.types;

/**
 *
 * @author Édson Fischborn
 */
public class CmbOption {
    String option;
    String value;

    public CmbOption(String option, String value){
        setOption(option);
        setValue(value);
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.getOption();
    }
}
