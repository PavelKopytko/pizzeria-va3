package by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto;

public class SelectedItemDto {

    private long id;
    private long dtUpdate;
    private long menuRow;
    private int count;
    private long order;

    public SelectedItemDto() {
    }

    public SelectedItemDto(long menuRow, int count) {
        this.menuRow = menuRow;
        this.count = count;
    }

    public SelectedItemDto(long id, long dtUpdate, long menuRow, int count, long order) {
        this.id = id;
        this.dtUpdate = dtUpdate;
        this.menuRow = menuRow;
        this.count = count;
        this.order = order;
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

    public long getMenuRow() {
        return menuRow;
    }

    public void setMenuRow(long menuRow) {
        this.menuRow = menuRow;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }
}
