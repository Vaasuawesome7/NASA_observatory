package com.example.nasagallery;

import java.util.List;

public class ImageAndVideo {
    private final Collection collection;

    public ImageAndVideo(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public static class Collection {
        private final List<Items> items;

        private final Metadata metadata;

        private final String version;

        private final List<Links> links;

        private final String href;

        public Collection(List<Items> items, Metadata metadata, String version, List<Links> links,
                          String href) {
            this.items = items;
            this.metadata = metadata;
            this.version = version;
            this.links = links;
            this.href = href;
        }

        public List<Items> getItems() {
            return items;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public String getVersion() {
            return version;
        }

        public List<Links> getLinks() {
            return links;
        }

        public String getHref() {
            return href;
        }

        public static class Items {
            private final List<Data> data;

            private final List<Links> links;

            private final String href;

            public Items(List<Data> data, List<Links> links, String href) {
                this.data = data;
                this.links = links;
                this.href = href;
            }

            public List<Data> getData() {
                return data;
            }

            public List<Links> getLinks() {
                return links;
            }

            public String getHref() {
                return href;
            }

            public static class Data {
                private final String description;

                private final String dateCreated;

                private final String nasa_id;

                private final List<String> keywords;

                private final String center;

                private final String title;

                private final String media_type;

                private final String description508;

                public Data(String description, String dateCreated, String nasaId,
                            List<String> keywords, String center, String title, String mediaType,
                            String description508) {
                    this.description = description;
                    this.dateCreated = dateCreated;
                    this.nasa_id = nasaId;
                    this.keywords = keywords;
                    this.center = center;
                    this.title = title;
                    this.media_type = mediaType;
                    this.description508 = description508;
                }

                public String getDescription() {
                    return description;
                }

                public String getDateCreated() {
                    return dateCreated;
                }

                public String getNasaId() {
                    return nasa_id;
                }

                public List<String> getKeywords() {
                    return keywords;
                }

                public String getCenter() {
                    return center;
                }

                public String getTitle() {
                    return title;
                }

                public String getMediaType() {
                    return media_type;
                }

                public String getDescription508() {
                    return description508;
                }
            }

            public static class Links {
                private final String rel;

                private final String render;

                private final String href;

                public Links(String rel, String render, String href) {
                    this.rel = rel;
                    this.render = render;
                    this.href = href;
                }

                public String getRel() {
                    return rel;
                }

                public String getRender() {
                    return render;
                }

                public String getHref() {
                    return href;
                }
            }
        }

        public static class Metadata {
            private final int totalHits;

            public Metadata(int totalHits) {
                this.totalHits = totalHits;
            }

            public int getTotalHits() {
                return totalHits;
            }
        }

        public static class Links {
            private final String href;

            private final String rel;

            private final String prompt;

            public Links(String href, String rel, String prompt) {
                this.href = href;
                this.rel = rel;
                this.prompt = prompt;
            }

            public String getHref() {
                return href;
            }

            public String getRel() {
                return rel;
            }

            public String getPrompt() {
                return prompt;
            }
        }
    }
}
