/*
 * Copyright 2017-2018   Eficode Oy
 * Copyright 2018-       Robot Framework Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javafxlibrary.testapps.customcomponents;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Statistic {

    private final SimpleStringProperty player = new SimpleStringProperty("");
    private final SimpleIntegerProperty maps = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty plusminus = new SimpleIntegerProperty(0);
    private final SimpleDoubleProperty kdr = new SimpleDoubleProperty(0.00);
    private final SimpleDoubleProperty rating = new SimpleDoubleProperty(0.00);

    public String getPlayer() {
        return player.get();
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public int getMaps() {
        return maps.get();
    }

    public void setMaps(int maps) {
        this.maps.set(maps);
    }

    public int getPlusminus() {
        return plusminus.get();
    }

    public void setPlusminus(int plusminus) {
        this.plusminus.set(plusminus);
    }

    public double getKdr() {
        return kdr.get();
    }

    public void setKdr(double kdr) {
        this.kdr.set(kdr);
    }

    public double getRating() {
        return rating.get();
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

}