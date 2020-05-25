package lib;

public class Word implements Comparable<Word>{
    public Word(String word, int count){
        this.word = word;
        this.count = count;
    }
    public String getWord(){ return this.word; }
    public int getCount(){ return this.count; }

    public void setWord(String word){ this.word = word; }
    public void setCount(int count){ this.count = count; }

    public void inc(){ this.count++; }

    private String word;
    private int count;

    @Override
    public int compareTo(Word obj) {
        return obj.getCount() - this.count;
    }
}