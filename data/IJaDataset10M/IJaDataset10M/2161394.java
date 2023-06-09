package com.hongbo.cobweb.nmr.converter.scan;

import com.hongbo.cobweb.nmr.converter.PackageScanFilter;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <code>CompositePackageScanFilter</code> allows multiple
 * {@link PackageScanFilter}s to be composed into a single filter. For a
 * {@link Class} to match a {@link CompositePackageScanFilter} it must match
 * each of the filters the composite contains
 */
public class CompositePackageScanFilter implements PackageScanFilter {

    private Set<PackageScanFilter> filters;

    public CompositePackageScanFilter() {
        filters = new LinkedHashSet<PackageScanFilter>();
    }

    public CompositePackageScanFilter(Set<PackageScanFilter> filters) {
        this.filters = new LinkedHashSet<PackageScanFilter>(filters);
    }

    public void addFilter(PackageScanFilter filter) {
        filters.add(filter);
    }

    public boolean matches(Class<?> type) {
        for (PackageScanFilter filter : filters) {
            if (!filter.matches(type)) {
                return false;
            }
        }
        return true;
    }
}
