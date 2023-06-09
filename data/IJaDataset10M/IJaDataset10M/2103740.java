package com.croftsoft.core.role;

/*********************************************************************
     * Determines whether an object should be filtered.
     *
     * @author
     *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
     * @version
     *   2003-04-09
     * @since
     *   2003-04-09
     *********************************************************************/
public interface Filter {

    /*********************************************************************
     * Filtrate is what passes through a filter.
     *
     * @return
     *
     *   Returns false if the object should be filtered.
     *********************************************************************/
    public boolean isFiltrate(Object o);
}
