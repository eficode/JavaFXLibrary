package testutils;

public class DelayedObjectRemoval {

    private Object object;
    private int timeout;
    private boolean finished;

    public DelayedObjectRemoval(Object object, int milliseconds) {
        this.object = object;
        this.timeout = milliseconds;
        this.finished = false;
    }

    public void start() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(this.timeout);
                this.finished = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    // Returns object if timeout has not finished / null if timeout has finished
    public Object getValue() {
        if (this.finished)
            return null;
        return this.object;
    }
}
