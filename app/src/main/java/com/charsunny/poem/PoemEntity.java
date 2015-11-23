package com.charsunny.poem;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

@Table(name = "poem", id = "pid")
public class PoemEntity extends Model {
    @Column(name = "pid")
    public int pid;

    @Column(name = "poet_id")
    public int aid;

    @Column(name = "name_cn")
    public String name;

    @Column(name = "format_id")
    public int formatId;

    @Column(name = "text_cn")
    public String content;

    public static PoemEntity getRandom() {
        return new Select().from(PoemEntity.class).orderBy("RANDOM()").executeSingle();
    }

    public static PoemEntity getPoemById(int id) {
        return new Select().from(PoemEntity.class).where("pid = ?", id).executeSingle();
    }

    public static List<PoemEntity> getPoemsByAuthor(int aid) {
        return new Select().from(PoemEntity.class).where("poet_id = ?", aid).execute();
    }
}
