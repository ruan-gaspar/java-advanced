import { User } from "./user.model";
import { Restaurant } from "./restaurant.model";

export interface Recommendation {
  user: User;
  restaurants: Restaurant[];
  ruleBasedExplanation: string;
  aiSuggestion: string;
}
