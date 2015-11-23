package com.charsunny.poem;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "poet", id = "pid")
public class AuthorEntity extends Model{
    @Column(name = "pid")
    public int pid;

    @Column(name = "period_id")
    public int period;

    @Column(name = "name_cn")
    public String name;

    @Column(name = "description_cn")
    public String description;

    public static AuthorEntity getPoetById(int id) {
        return new Select().from(AuthorEntity.class).where("pid = ?", id).executeSingle();
    }
}
