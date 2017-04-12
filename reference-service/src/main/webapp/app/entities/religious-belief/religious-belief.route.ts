import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {ReligiousBeliefComponent} from "./religious-belief.component";
import {ReligiousBeliefDetailComponent} from "./religious-belief-detail.component";
import {ReligiousBeliefPopupComponent} from "./religious-belief-dialog.component";
import {ReligiousBeliefDeletePopupComponent} from "./religious-belief-delete-dialog.component";

@Injectable()
export class ReligiousBeliefResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const religiousBeliefRoute: Routes = [
	{
		path: 'religious-belief',
		component: ReligiousBeliefComponent,
		resolve: {
			'pagingParams': ReligiousBeliefResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.religiousBelief.home.title'
		}
	}, {
		path: 'religious-belief/:id',
		component: ReligiousBeliefDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.religiousBelief.home.title'
		}
	}
];

export const religiousBeliefPopupRoute: Routes = [
	{
		path: 'religious-belief-new',
		component: ReligiousBeliefPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.religiousBelief.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'religious-belief/:id/edit',
		component: ReligiousBeliefPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.religiousBelief.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'religious-belief/:id/delete',
		component: ReligiousBeliefDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.religiousBelief.home.title'
		},
		outlet: 'popup'
	}
];
