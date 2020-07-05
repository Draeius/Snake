
class InvalidPositionException extends Exception {

    private static final long serialVersionUID = 4688199528301999464L;

    private Position pos;
    private Direction dir;

    public InvalidPositionException(Position pos){
        super();
        this.pos = pos;
        dir = null;
    }

    public InvalidPositionException(Position pos, Direction dir){
        super();
        this.pos = pos;
        this.dir = dir;
    }

    public String toString(){
        String str = super.toString();
        str += String.format("\r\n X: %s; Y: %s; Direction: %s", pos.getX(), pos.getY(), dir);
        return str;
    }

}