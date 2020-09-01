package com.example.nasagallery;

import java.util.List;

@SuppressWarnings("all")
public class RecievedItems {
    private final Collection collection;

    public RecievedItems(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public static class Collection {
        private final String href;

        private final List<Items> items;

        private final String version;

        public Collection(String href, List<Items> items, String version) {
            this.href = href;
            this.items = items;
            this.version = version;
        }

        public String getHref() {
            return href;
        }

        public List<Items> getItems() {
            return items;
        }

        public String getVersion() {
            return version;
        }

        public static class Items {
            private final String href;

            public Items(String href) {
                this.href = href;
            }

            public String getHref() {
                return href;
            }
        }
    }
}
