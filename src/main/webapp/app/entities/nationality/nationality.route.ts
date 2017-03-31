import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {NationalityComponent} from "./nationality.component";
import {NationalityDetailComponent} from "./nationality-detail.component";
import {NationalityPopupComponent} from "./nationality-dialog.component";
import {NationalityDeletePopupComponent} from "./nationality-delete-dialog.component";

@Injectable()
export class NationalityResolvePagingParams implements Resolve<any> {

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

export const nationalityRoute: Routes = [
	{
		path: 'nationality',
		component: NationalityComponent,
		resolve: {
			'pagingParams': NationalityResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.nationality.home.title'
		}
	}, {
		path: 'nationality/:id',
		component: NationalityDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.nationality.home.title'
		}
	}
];

export const nationalityPopupRoute: Routes = [
	{
		path: 'nationality-new',
		component: NationalityPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.nationality.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'nationality/:id/edit',
		component: NationalityPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.nationality.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'nationality/:id/delete',
		component: NationalityDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.nationality.home.title'
		},
		outlet: 'popup'
	}
];
