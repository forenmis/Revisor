package com.example.revizor.data.provider;

import com.example.revizor.R;
import com.example.revizor.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemProvider {
    public static List<Item> items() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when" +
                " an unknown printer took a galley of type and scrambled it to make a type specimen" +
                " book. It has survived not only five centuries, but also the leap into electronic" +
                " typesetting, remaining essentially unchanged." +
                "versions of Lorem Ipsum.", R.drawable.frame_first));
        items.add(new Item("Simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when" +
                " an unknown printer took a galley of type and scrambled it to make a type specimen" +
                " book. It has survived not only five centuries, but also the leap into electronic" +
                " typesetting, remaining essentially unchanged." +
                "versions of Lorem Ipsum.", R.drawable.frame_woman));
        items.add(new Item("Simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when" +
                " an unknown printer took a galley of type and scrambled it to make a type specimen" +
                " book. It has survived not only five centuries, but also the leap into electronic" +
                " typesetting, remaining essentially unchanged." +
                "versions of Lorem Ipsum.", R.drawable.frame_man_with_cup));
        return items;

    }
}
