package com.nuevatel.bcf;

/**
 * Get from configuration the NameGetter Action class.
 *
 * @author Ariel Salazar
 */
public final class NameGetterProvider {

    private static NameGetter nameGetter = null;

    private NameGetterProvider() {
        // No op. Used to prevent the instantiation.
    }

    /**
     * Load Name getter class, to instantiate it. NameGetter Implementation must default constructor.
     *
     * @param clazzNameGetter
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public synchronized static void set(Class<NameGetter>clazzNameGetter) throws IllegalAccessException,
                                                                                 InstantiationException {
        if (clazzNameGetter == null) {
            return;
        }
        nameGetter =  clazzNameGetter.newInstance();
    }

    /**
     *
     * @return Get an instance of NameGetter.
     */
    public synchronized static NameGetter get() {
        return nameGetter;
    }
}
