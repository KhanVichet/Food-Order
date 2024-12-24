package com.example.foodapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.foodapp.Domain.FoodDomain;
import com.example.foodapp.Interface.ChangerNumberitemsListener;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(FoodDomain item) {
        ArrayList<FoodDomain> listFood = getListCart();
        if (listFood == null) {
            listFood = new ArrayList<>();
        }

        boolean existAlready = false;
        int n = 0;

        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if (existAlready) {
            listFood.get(n).setNumberInCart(listFood.get(n).getNumberInCart() + item.getNumberInCart());
        } else {
            listFood.add(item);
        }

        tinyDB.putListObject("CardList", listFood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodDomain> getListCart() {
        ArrayList<FoodDomain> listFood = tinyDB.getListObject("CardList", FoodDomain.class);
        return listFood == null ? new ArrayList<>() : listFood;
    }

    public void minusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangerNumberitemsListener changerNumberitemsListener) {
        if (listFood.get(position).getNumberInCart() == 1) {
            listFood.remove(position);
        } else {
            listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CardList", listFood);
        changerNumberitemsListener.changed();
    }

    public void plusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangerNumberitemsListener changerNumberitemsListener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CardList", listFood);
        changerNumberitemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<FoodDomain> listFood = getListCart();
        double fee = 0;
        for (int i = 0; i < listFood.size(); i++) {
            fee += listFood.get(i).getFee() * listFood.get(i).getNumberInCart();
        }
        return fee;
    }
}
