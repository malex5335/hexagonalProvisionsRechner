package org.example;

public class StackInfoUtil {

    private StackInfoUtil() {
        // no need for a constructor, therefore private
    }

    /**
     * for building individual caching keys you often need the method name which could change<br>
     * with this method even changing does not have an impact on the integrity of the caching key
     *
     * @return the method name of the method that calls this one
     */
    public static String getCurrentMethodName() {
        StackWalker walker = StackWalker.getInstance();
        var result = walker.walk(frames -> frames
                        .skip(1) // skip myself
                        .findFirst()
                        .map(StackWalker.StackFrame::getMethodName));
        if(result.isPresent()) {
           return result.get();
        }
        throw new RuntimeException("could not retrieve the current method name");
    }

    /**
     * for building individual caching keys you often need the class name which could change<br>
     * with this method even changing does not have an impact on the integrity of the caching key
     *
     * @return the class name of the method that calls this one
     */
    public static String getCurrentClassName() {
        StackWalker walker = StackWalker.getInstance();
        var result = walker.walk(frames -> frames
                .skip(1) // skip myself
                .findFirst()
                .map(StackWalker.StackFrame::getClassName));
        if(result.isPresent()) {
            return result.get();
        }
        throw new RuntimeException("could not retrieve the current class name");
    }
}
