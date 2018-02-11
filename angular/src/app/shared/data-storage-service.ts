import {Injectable} from "@angular/core";
import {RecipeService} from "../recipes/recipe.service";
import {Recipe} from "../recipes/recipe.model";
import "rxjs/add/operator/map";
import {AuthService} from "../auth/auth-service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataStorageService {

  constructor(private httpService: HttpClient, private recipeService: RecipeService, private authService: AuthService) {}

  storeRecipes() {
    return this.httpService.put('https://angularrecipes-525fe.firebaseio.com/recipes.json', this.recipeService.getRecipes(),
      {observe: 'body'});
  }

  getRecipes() {
    this.httpService.get<Recipe[]>('https://angularrecipes-525fe.firebaseio.com/recipes.json')
      .map((recipes) => {
        for (let recipe of recipes) {
          if (!recipe['ingredients']) {
            recipe['ingredients'] = [];
          }
        }
        return recipes;
      })
      .subscribe(
      (recipes: Recipe[]) => {
        this.recipeService.setRecipes(recipes);
      }
    );
  }
}
