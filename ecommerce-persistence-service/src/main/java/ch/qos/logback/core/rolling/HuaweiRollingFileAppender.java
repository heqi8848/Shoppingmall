package ch.qos.logback.core.rolling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by boris on 8/21/17.
 */
public class HuaweiRollingFileAppender<E> extends ch.qos.logback.core.rolling.RollingFileAppender<E> {
    @Override
    public void openFile(String file_name) throws IOException {
        this.lock.lock();
        try {
            final Set<PosixFilePermission> perms = EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.GROUP_READ);
            super.openFile(file_name);
            Files.setPosixFilePermissions(Paths.get(file_name), perms);
        } finally {
            this.lock.unlock();
        }
    }
}
