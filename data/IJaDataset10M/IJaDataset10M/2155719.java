package org.eclipse.jdt.core.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

/**
 * A Java search match that represents a type parameter declaration or reference.
 * The element is an {@link org.eclipse.jdt.core.ITypeParameter}.
 * <p>
 * This class is intended to be instantiated and subclassed by clients.
 * </p>
 * 
 * @since 3.1
 */
public class TypeParameterDeclarationMatch extends SearchMatch {

    /**
	 * Creates a new type parameter match.
	 * 
	 * @param element the type parameter
	 * @param accuracy one of A_ACCURATE or A_INACCURATE
	 * @param offset the offset the match starts at, or -1 if unknown
	 * @param length the length of the match, or -1 if unknown
	 * @param participant the search participant that created the match
	 * @param resource the resource of the element
	 */
    public TypeParameterDeclarationMatch(IJavaElement element, int accuracy, int offset, int length, SearchParticipant participant, IResource resource) {
        super(element, accuracy, offset, length, participant, resource);
    }
}
