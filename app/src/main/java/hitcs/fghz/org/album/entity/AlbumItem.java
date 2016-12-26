package hitcs.fghz.org.album.entity;

/**
 * 相册中的元素---一个相册的组成
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
