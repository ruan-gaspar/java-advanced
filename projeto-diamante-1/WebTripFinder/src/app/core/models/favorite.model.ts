export interface FavoriteRequest {
  id: string;
  name: string;
  category?: string | null;
  address?: string | null;
  city?: string | null;
  country?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  imageUrl?: string | null;
}

export interface FavoriteResponse {
  id: number;
  externalPlaceId: string;
  name: string;
  category?: string | null;
  address?: string | null;
  city?: string | null;
  country?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  imageUrl?: string | null;
  savedAt: string;
}
