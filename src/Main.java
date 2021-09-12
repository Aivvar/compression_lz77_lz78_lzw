import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public ArrayList<LZ77_object> lz77 = new ArrayList<>();
    public ArrayList<LZ78_object> lz78 = new ArrayList<>();
    public ArrayList<Integer> lzw = new ArrayList<>();
    public ArrayList<String> lz78_strings = new ArrayList<>();
    public ArrayList<String> lzw_strings = new ArrayList<>();
    public int uprezh_length = 20, poisk_length = 1000;
    public StringBuilder output_code;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("input.txt"));
        StringBuilder input_text = new StringBuilder();
        while (input.hasNext())
            input_text.append(input.nextLine());// + "\r\n";
        input.close();
        System.out.println(input_text);
        new Main().lzw_coding(input_text.toString());
    }

    public void lz77_coding(String input_str) {
        int position = 1, sovp_count, offset = 0, ind_of;
        System.out.println("---------LZ77_Coding----------");
        String bufer_poiska = "", uprezh_buffer;
        uprezh_buffer = input_str.substring(position, position + 20);
        lz77.add(new LZ77_object(0, 0, input_str.toCharArray()[0]));
        System.out.println("<0,0," + input_str.toCharArray()[0] + ">");
        bufer_poiska += input_str.toCharArray()[0];
        while (position < input_str.length() - 1) {
            System.out.println("Буфер поиска: " + bufer_poiska);
            System.out.println("Упреждающий буфер: " + uprezh_buffer);
            StringBuilder temp_str = new StringBuilder();
            sovp_count = 0;
            do {
                if (sovp_count < uprezh_buffer.length())
                    temp_str.append(uprezh_buffer.toCharArray()[sovp_count]);
                ind_of = bufer_poiska.indexOf(temp_str.toString());
                if (ind_of == -1) {
                    break;
                } else {
                    offset = ind_of;
                    if (position + sovp_count == input_str.length() - 1)
                        break;
                    sovp_count++;
                }

            }
            while (true);

            if (sovp_count == 0) {
                lz77.add(new LZ77_object(0, 0, uprezh_buffer.toCharArray()[0]));
                System.out.println("<0,0," + uprezh_buffer.toCharArray()[0] + ">");
                bufer_poiska += uprezh_buffer.toCharArray()[0];
                position++;
            } else {
                lz77.add(new LZ77_object(offset, sovp_count, uprezh_buffer.toCharArray()[sovp_count]));
                System.out.println("<" + offset + "," + sovp_count + "," + uprezh_buffer.toCharArray()[sovp_count] + ">");
                bufer_poiska += uprezh_buffer.substring(0, sovp_count);
                position += sovp_count;
            }
            if (position <= input_str.length() - 1) {
                if (position + 20 <= input_str.length() - 1)
                    uprezh_buffer = input_str.substring(position, position + 20);
                else uprezh_buffer = input_str.substring(position);
            } else break;
        }
        StringBuilder coding_length, coding_offset, coding_next_char;
        output_code = new StringBuilder();
        for (LZ77_object lz : lz77) {
            coding_length = new StringBuilder();
            coding_offset = new StringBuilder();
            coding_next_char = new StringBuilder();
            coding_offset.append(Integer.toString(lz.offset, 2));
            if(coding_offset.length()==6)
                System.out.println("Больше 6");
            while (coding_offset.length() < 5)
            {
                coding_offset.insert(0, "0");
            }

            coding_length.append(Integer.toString(lz.length, 2));
            while (coding_length.length() < 5)
            {
                coding_length.insert(0, "0");}
            coding_next_char.append(Integer.toBinaryString(lz.next));
            while (coding_next_char.length() < 8)
                coding_next_char.insert(0, "0");
            output_code.append(coding_offset);
            output_code.append(coding_length);
            output_code.append(coding_next_char);
            System.out.println(coding_offset.length()+" "+coding_length.length()+" "+coding_next_char.length());
        }

        System.out.println("Размер "+lz77.size());
        System.out.println("Длина "+output_code.length());
        lz77_decoding();
    }

    public void lz77_decoding() {
        int offset, length, pos = 0;
        char next;
        System.out.println("---------LZ77_Decoding----------");
        StringBuilder bufer_poiska = new StringBuilder();
        StringBuilder decode_str = new StringBuilder();
        do {
            offset = Integer.parseInt(output_code.substring(pos, pos + 5), 2);
            pos += 5;
            length = Integer.parseInt(output_code.substring(pos, pos + 5), 2);
            pos += 5;
            next = (char) Integer.parseInt(output_code.substring(pos, pos + 8), 2);
            pos += 8;
            if (offset == 0 && length == 0) {
                bufer_poiska.append(next);
                decode_str.append(next);
            } else {
                decode_str.append(bufer_poiska.substring(offset, offset + length));
                bufer_poiska.append((bufer_poiska.substring(offset, offset + length)));
            }
            if (pos >= output_code.length() - 1)
                decode_str.append(next);

        }
        while (pos < output_code.length() - 1);
        System.out.println(decode_str);
    }

    public void lz78_coding(String input_str) {

        int position = 1, last_ind, curr_ind;
        char diff;
        System.out.println("---------LZ78_Coding----------");
        lz78_strings.add("");
        System.out.println("Добавлено " + "");
        lz78_strings.add(input_str.toCharArray()[0] + "");
        System.out.println("Добавлено " + lz78_strings.get(lz78_strings.size() - 1));
        lz78.add(new LZ78_object(0, input_str.toCharArray()[0]));
        StringBuilder temp_str = new StringBuilder();
        int k = 1;
        while (position < input_str.length() - 1) {
            temp_str.append(input_str.toCharArray()[position]);
            curr_ind = lz78_strings.indexOf(temp_str.toString());
            if (curr_ind == -1) {
                lz78_strings.add(temp_str.toString());
                System.out.println("Добавлено " + lz78_strings.get(lz78_strings.size() - 1));
                lz78.add(new LZ78_object(0, temp_str.charAt(0)));
                k++;
                position++;
            } else do {
                if (position < input_str.length() - 1) {
                    position++;
                    last_ind = curr_ind;
                    temp_str.append(input_str.toCharArray()[position]);
                    curr_ind = lz78_strings.indexOf(temp_str.toString());
                    if (curr_ind == -1) {
                        diff = temp_str.charAt(temp_str.length() - 1);
                        lz78_strings.add(temp_str.toString());
                        System.out.println("Добавлено " + lz78_strings.get(lz78_strings.size() - 1));
                        lz78.add(new LZ78_object(last_ind, diff));
                        position++;
                    } else if (position >= input_str.length() - 1)
                        lz78.add(new LZ78_object(curr_ind, '!'));

                } else // {lz78.add(new LZ78_object(curr_ind, '_'));
                    break;
                //  }
            }
            while (curr_ind != -1);
            temp_str = new StringBuilder();
        }
        for (
                LZ78_object lz : lz78) {
            System.out.println("<" + lz.index + ", " + lz.added + ">");
        }
        int sch = 0;
        System.out.println("Словарь");
        for (
                String lz : lz78_strings) {
            System.out.println(sch + " <" + lz + ">");
            sch++;
        }
        StringBuilder coding_offset, coding_next_char;
        output_code = new StringBuilder();
        for (LZ78_object lz : lz78) {
            coding_offset = new StringBuilder();
            coding_next_char = new StringBuilder();
            coding_offset.append(Integer.toString(lz.index, 2));
            while (coding_offset.length() < 10)
                coding_offset.insert(0, "0");
            coding_next_char.append(Integer.toBinaryString(lz.added));
            while (coding_next_char.length() < 8)
                coding_next_char.insert(0, "0");
            output_code.append(coding_offset);
            output_code.append(coding_next_char);
        }

        lz78_decoding();
    }

    public void lz78_decoding() {
        System.out.println("---------LZ78_Decoding----------");
        int index, pos = 0;
        char next;
        StringBuilder decode_str = new StringBuilder();
        System.out.println("Размер " + output_code.length());
        do {
            index = Integer.parseInt(output_code.substring(pos, pos + 10), 2);
            pos += 10;
            next = (char) Integer.parseInt(output_code.substring(pos, pos + 8), 2);
            pos += 8;
            if (index != 0) decode_str.append(lz78_strings.get(index));
            if (next != '!') decode_str.append(next);

        }
        while (pos < output_code.length() - 1);
        System.out.println(decode_str);
    }

    public void lzw_coding(String input_str) {
        System.out.println("---------LZW_Coding----------");
        int pos = 1, cur_ind = 0, ind_temp, temp_curr = 0;
       // char last = input_str.toCharArray()[input_str.length() - 1];
       // input_str+=" ";
        lzw_strings.add(input_str.toCharArray()[0] + "");
        String temp_str = "";
        for (int k = 0; k <= input_str.length() - 1; k++)
            if (lzw_strings.indexOf(input_str.toCharArray()[k]+"") == -1)
                lzw_strings.add(input_str.toCharArray()[k] + "");
        while (pos <= input_str.length() - 1) {
            temp_str += input_str.toCharArray()[pos - 1];
            temp_str += input_str.toCharArray()[pos];
            cur_ind = lzw_strings.indexOf(temp_str);
            if (cur_ind == -1) {
                lzw.add(lzw_strings.indexOf(temp_str.toCharArray()[0] + ""));
                lzw_strings.add(temp_str);
            } else {
                ind_temp = pos;
                do {
                    ind_temp++;
                    temp_curr=cur_ind;
                    if (ind_temp == input_str.length() - 1)
                        break;
                    temp_str += input_str.toCharArray()[ind_temp];
                    cur_ind = lzw_strings.indexOf(temp_str);
                }
                while (cur_ind != -1);
                lzw.add(temp_curr);

                lzw_strings.add(temp_str);
            }
            System.out.println("Строка: "+temp_str);
            if (pos + temp_str.length() <= input_str.length()+1) {
                pos += temp_str.length() - 1;
            }
            else break;
            temp_str = "";
            if(pos==input_str.length()) {
                lzw.add(lzw_strings.indexOf(input_str.toCharArray()[pos-1] + ""));
                break;
            }
        }
        int sch = 0;
        for (
                String lz : lzw_strings) {
            System.out.println(sch + " <" + lz + ">");
            sch++;
        }
        for (int lzw_ind : lzw) {
            System.out.println("<" + lzw_ind + ">");
        }
        StringBuilder coding_index;
        output_code = new StringBuilder();
        for (int lz : lzw) {
            coding_index = new StringBuilder();
            coding_index.append(Integer.toString(lz, 2));
            while (coding_index.length() < 10)
                coding_index.insert(0, "0");
            output_code.append(coding_index);
        }
            lzw_decoding();
    }

    public void lzw_decoding() {
        System.out.println("---------LZW_Decoding----------");
        int index, pos = 0;
        StringBuilder decode_str = new StringBuilder();
        do {
            index = Integer.parseInt(output_code.substring(pos, pos + 10), 2);
            pos += 10;
            decode_str.append(lzw_strings.get(index));
        }
        while (pos < output_code.length() - 1);
        System.out.println(decode_str);

    }
}
