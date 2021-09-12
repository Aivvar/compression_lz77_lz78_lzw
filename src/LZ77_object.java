public class LZ77_object {
  int offset,length;
  char next;

    public LZ77_object(int offset, int length, char next) {
        this.offset = offset;
        this.length = length;
        this.next = next;
    }
}
