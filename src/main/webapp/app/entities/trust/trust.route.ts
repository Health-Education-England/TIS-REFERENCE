import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {TrustComponent} from "./trust.component";
import {TrustDetailComponent} from "./trust-detail.component";
import {TrustPopupComponent} from "./trust-dialog.component";
import {TrustDeletePopupComponent} from "./trust-delete-dialog.component";

@Injectable()
export class TrustResolvePagingParams implements Resolve<any> {

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

export const trustRoute: Routes = [
	{
		path: 'trust',
		component: TrustComponent,
		resolve: {
			'pagingParams': TrustResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trust.home.title'
		}
	}, {
		path: 'trust/:id',
		component: TrustDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trust.home.title'
		}
	}
];

export const trustPopupRoute: Routes = [
	{
		path: 'trust-new',
		component: TrustPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trust.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'trust/:id/edit',
		component: TrustPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trust.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'trust/:id/delete',
		component: TrustDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trust.home.title'
		},
		outlet: 'popup'
	}
];
