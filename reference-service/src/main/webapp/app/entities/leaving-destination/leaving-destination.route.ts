import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {LeavingDestinationComponent} from "./leaving-destination.component";
import {LeavingDestinationDetailComponent} from "./leaving-destination-detail.component";
import {LeavingDestinationPopupComponent} from "./leaving-destination-dialog.component";
import {LeavingDestinationDeletePopupComponent} from "./leaving-destination-delete-dialog.component";

@Injectable()
export class LeavingDestinationResolvePagingParams implements Resolve<any> {

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

export const leavingDestinationRoute: Routes = [
	{
		path: 'leaving-destination',
		component: LeavingDestinationComponent,
		resolve: {
			'pagingParams': LeavingDestinationResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.leavingDestination.home.title'
		}
	}, {
		path: 'leaving-destination/:id',
		component: LeavingDestinationDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.leavingDestination.home.title'
		}
	}
];

export const leavingDestinationPopupRoute: Routes = [
	{
		path: 'leaving-destination-new',
		component: LeavingDestinationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.leavingDestination.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'leaving-destination/:id/edit',
		component: LeavingDestinationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.leavingDestination.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'leaving-destination/:id/delete',
		component: LeavingDestinationDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.leavingDestination.home.title'
		},
		outlet: 'popup'
	}
];
