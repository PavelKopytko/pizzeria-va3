package by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto;


public class MenuDto {

    private long id;
    private long dtUpdate;
    private String name;
    private boolean enable;

    public MenuDto() {
    }

    public MenuDto(long id, long dtUpdate, String name, boolean enable) {
        this.id = id;
        this.dtUpdate = dtUpdate;
        this.name = name;
        this.enable = enable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(long dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
