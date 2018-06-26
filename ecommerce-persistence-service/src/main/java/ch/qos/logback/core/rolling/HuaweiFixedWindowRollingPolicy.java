package ch.qos.logback.core.rolling;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by boris on 8/21/17.
 */
public class HuaweiFixedWindowRollingPolicy extends ch.qos.logback.core.rolling.FixedWindowRollingPolicy {
    @Override
    public void rollover() throws RolloverFailure {
        String fileName = this.fileNamePattern.convertInt(this.minIndex);
        super.rollover();
        File file = new File(fileName);
        if (file.exists()) {
            final Set<PosixFilePermission> perms = EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.GROUP_READ);
            try {
                Files.setPosixFilePermissions(file.toPath(), perms);
            } catch (IOException e) {
                throw new RolloverFailure("Failed to rollover the file " + fileName, e);
            }
        }
    }
}
