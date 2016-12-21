package hitcs.fghz.org.album.entity;

/**
 * Created by me on 16-12-21.
 */

public class AlbumItem {
        private String name;
        private int imageId;
        public AlbumItem(String name, int imageId) {
            this.name = name;
            this.imageId = imageId;

        }
        public String getName() {
            return name;
        }
        public int getImageId() {
            return imageId;
        }
}
