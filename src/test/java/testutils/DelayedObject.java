package testutils;

public class DelayedObject {

    private Object object;
    private int timeout;
    private boolean finished;

    public DelayedObject(Object object, int milliseconds) {
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

    // Returns null if timeout has not finished / object if timeout has finished
    public Object getValue() {
        if (this.finished)
            return this.object;
        return null;
    }
}
