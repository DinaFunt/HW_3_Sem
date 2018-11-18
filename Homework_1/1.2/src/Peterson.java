public class Peterson {
    private boolean[] flag;
    private int victim;

    public Peterson() {
        victim = -1;
        flag = new boolean[2];
    }

    public void lock(int id) {
        flag[id] = true;
        victim  = id;
        while (flag[1 - id] && victim == id) {};
    }

    public void unlock(int id) {
        flag[id] = false;
    }
}
