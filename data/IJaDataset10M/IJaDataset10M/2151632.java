package com.coi.tools.os.win;

/**
 * Constants related to MS Windows DACLs.
 *
 * @author Klaus Bartz
 */
public interface MSWinConstants {

    /**
     * HKCR registry root
     */
    static final int HKEY_CLASSES_ROOT = 0x80000000;

    /**
     * HKCU registry root
     */
    static final int HKEY_CURRENT_USER = 0x80000001;

    /**
     * HKLM registry root
     */
    static final int HKEY_LOCAL_MACHINE = 0x80000002;

    /**
     * HKU registry root
     */
    static final int HKEY_USERS = 0x80000003;

    /**
     * HKPD registry root
     */
    static final int HKEY_PERFORMANCE_DATA = 0x80000004;

    /**
     * HKCC registry root
     */
    static final int HKEY_CURRENT_CONFIG = 0x80000005;

    /**
     * HKDD registry root
     */
    static final int HKEY_DYN_DATA = 0x80000006;

    /**
     * No value type
     */
    static final int REG_NONE = 0;

    /**
     * Unicode nul terminated string
     */
    static final int REG_SZ = 1;

    /**
     * Unicode nul terminated string
     */
    static final int REG_EXPAND_SZ = 2;

    /**
     * Free form binary
     */
    static final int REG_BINARY = 3;

    /**
     * 32-bit number
     */
    static final int REG_DWORD = 4;

    /**
     * Symbolic Link (unicode)
     */
    static final int REG_LINK = 6;

    /**
     * Multiple Unicode strings
     */
    static final int REG_MULTI_SZ = 7;

    /**
     * Flag for permission read file or pipe date.
     */
    static final int FILE_READ_DATA = 0x0001;

    /**
     * Flag for permission list contents of a directory.
     */
    static final int FILE_LIST_DIRECTORY = 0x0001;

    /**
     * Flag for permission write file or pipe data.
     */
    static final int FILE_WRITE_DATA = 0x0002;

    /**
     * Flag for permission add a file to a directory.
     */
    static final int FILE_ADD_FILE = 0x0002;

    /**
     * Flag for permission add data to a file (append).
     */
    static final int FILE_APPEND_DATA = 0x0004;

    /**
     * Flag for permission add a subdirectory to a directory.
     */
    static final int FILE_ADD_SUBDIRECTORY = 0x0004;

    /**
     * Flag for permission create a named pipe.
     */
    static final int FILE_CREATE_PIPE_INSTANCE = 0x0004;

    /**
     * Flag for permission read.
     */
    static final int FILE_READ_EA = 0x0008;

    /**
     * Flag for permission write.
     */
    static final int FILE_WRITE_EA = 0x0010;

    /**
     * Flag for permission execute a file.
     */
    static final int FILE_EXECUTE = 0x0020;

    /**
     * Flag for permission traverse through a directory.
     */
    static final int FILE_TRAVERSE = 0x0020;

    /**
     * Flag for permission delete a file or subdirectory in a directory.
     */
    static final int FILE_DELETE_CHILD = 0x0040;

    /**
     * Flag for permission all read attributes.
     */
    static final int FILE_READ_ATTRIBUTES = 0x0080;

    /**
     * Flag for permission all write attributes.
     */
    static final int FILE_WRITE_ATTRIBUTES = 0x0100;

    /**
     * Flag for permission delete.
     */
    static final int DELETE = 0x00010000;

    /**
     * Flag for permission read.
     */
    static final int READ_CONTROL = 0x00020000;

    /**
     * Flag for permission write a DAC.
     */
    static final int WRITE_DAC = 0x00040000;

    /**
     * Flag for permission set owner.
     */
    static final int WRITE_OWNER = 0x00080000;

    /**
     * Flag for permission use synchronize.
     */
    static final int SYNCHRONIZE = 0x00100000;

    /**
     * Flag for permission standard rights for required.
     */
    static final int STANDARD_RIGHTS_REQUIRED = 0x000F0000;

    /**
     * Flag for permission standard rights for read.
     */
    static final int STANDARD_RIGHTS_READ = 0x00020000;

    /**
     * Flag for permission standard rights for write.
     */
    static final int STANDARD_RIGHTS_WRITE = 0x00020000;

    /**
     * Flag for permission standard rights for execute.
     */
    static final int STANDARD_RIGHTS_EXECUTE = 0x00020000;

    /**
     * Flag for permission all standard rights.
     */
    static final int STANDARD_RIGHTS_ALL = 0x001F0000;

    /**
     * Flag for permission all specific rights.
     */
    static final int SPECIFIC_RIGHTS_ALL = 0x0000FFFF;

    /**
     * Flag for permission STANDARD_RIGHTS_REQUIRED | SYNCHRONIZE | 0x3FF.
     */
    static final int FILE_ALL_ACCESS = 0x001F03FF;

    /**
     * Flag for permission generic read.
     */
    static final int FILE_GENERIC_READ = 0x00120089;

    /**
     * Flag for permission generic write.
     */
    static final int FILE_GENERIC_WRITE = 0x00120116;

    /**
     * Flag for permission generic execute.
     */
    static final int FILE_GENERIC_EXECUTE = 0x001200A0;

    /**
     * Flag for permission all specific rights.
     */
    static final int ACCESS_SYSTEM_SECURITY = 0x01000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int MAXIMUM_ALLOWED = 0x02000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int GENERIC_READ = 0x80000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int GENERIC_WRITE = 0x40000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int GENERIC_EXECUTE = 0x20000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int GENERIC_ALL = 0x10000000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_CASE_SENSITIVE_SEARCH = 0x00000001;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_CASE_PRESERVED_NAMES = 0x00000002;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_UNICODE_ON_DISK = 0x00000004;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_PERSISTENT_ACLS = 0x00000008;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_FILE_COMPRESSION = 0x00000010;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_VOLUME_QUOTAS = 0x00000020;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_SUPPORTS_SPARSE_FILES = 0x00000040;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_SUPPORTS_REPARSE_POINTS = 0x00000080;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_SUPPORTS_REMOTE_STORAGE = 0x00000100;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_VOLUME_IS_COMPRESSED = 0x00008000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_SUPPORTS_OBJECT_IDS = 0x00010000;

    /**
     * Flag for NT permissions: For more information see the Windows NT description for permisson
     * flags.
     */
    static final int FILE_SUPPORTS_ENCRYPTION = 0x00020000;
}
