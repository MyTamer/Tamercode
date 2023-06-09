package suneido.language.builtin;

import java.util.concurrent.TimeUnit;
import suneido.database.server.DbmsServerBySocket;
import suneido.language.*;

public class Delayed extends BuiltinFunction {

    private static final FunctionSpec fs = new FunctionSpec("ms", "function");

    @Override
    public Object call(Object... args) {
        args = Args.massage(fs, args);
        DbmsServerBySocket.scheduler.schedule(new Run(args[1]), Ops.toInt(args[0]), TimeUnit.MILLISECONDS);
        return null;
    }

    private static class Run implements Runnable {

        private final Object fn;

        public Run(Object fn) {
            this.fn = fn;
        }

        public void run() {
            Ops.call(fn);
        }
    }
}
