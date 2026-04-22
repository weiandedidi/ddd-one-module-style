import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author maqidi
 * @version 1.0
 * @create 2026-04-22 14:51
 */
public class NormalTest {
    @Test
    void testExample() {
        assertThat("hello").isEqualTo("hello");
        assertThat(List.of(1, 2, 3)).isEqualTo(List.of(1, 2, 3));
    }
}
