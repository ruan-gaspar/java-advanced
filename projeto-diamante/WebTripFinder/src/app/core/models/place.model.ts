export interface PlaceSummary {
  id: string;
  name: string;
  category: string | null;
  address: string | null;
  city: string | null;
  country: string | null;
  latitude: number | null;
  longitude: number | null;
  distance: number | null;
  imageUrl: string | null;
}

export interface PlaceDetail {
  id: string;
  name: string;
  description: string | null;
  category: string | null;
  address: string | null;
  city: string | null;
  country: string | null;
  latitude: number | null;
  longitude: number | null;
  phone: string | null;
  website: string | null;
  imageUrl: string | null;
}
