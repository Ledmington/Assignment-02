package parser.info;

public record Range(int start, int end) {
    public Range {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("start and end must be >= 0");
        }
        if (start > end) {
            throw new IllegalArgumentException("start must be <= end");
        }
    }
}
