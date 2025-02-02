package request.tdo;

import java.io.Serializable;
import java.util.List;

public class CategoryDTO implements Serializable {
    private int categoryId;
    private String name;


    // Getters and setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryId=" + categoryId +
                ", name='" + name  +
                '}';
    }
}

