import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {InactiveReasonComponent} from "./inactive-reason.component";
import {InactiveReasonDetailComponent} from "./inactive-reason-detail.component";
import {InactiveReasonPopupComponent} from "./inactive-reason-dialog.component";
import {InactiveReasonDeletePopupComponent} from "./inactive-reason-delete-dialog.component";

@Injectable()
export class InactiveReasonResolvePagingParams implements Resolve<any> {

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

export const inactiveReasonRoute: Routes = [
	{
		path: 'inactive-reason',
		component: InactiveReasonComponent,
		resolve: {
			'pagingParams': InactiveReasonResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.inactiveReason.home.title'
		}
	}, {
		path: 'inactive-reason/:id',
		component: InactiveReasonDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.inactiveReason.home.title'
		}
	}
];

export const inactiveReasonPopupRoute: Routes = [
	{
		path: 'inactive-reason-new',
		component: InactiveReasonPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.inactiveReason.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'inactive-reason/:id/edit',
		component: InactiveReasonPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.inactiveReason.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'inactive-reason/:id/delete',
		component: InactiveReasonDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.inactiveReason.home.title'
		},
		outlet: 'popup'
	}
];
