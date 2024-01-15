package database

class Category {
    private var id:Int = 0
    private var title = String()

    public fun getTitle():String{
        return title;
    }

    public fun getId():Int{
        return id;
    }

    public fun setTitle(title: String){
        this.title = title;
    }

    public fun setId(id: Int){
        this.id = id;
    }
}