import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {SiteComponent} from "./site.component";
import {SiteDetailComponent} from "./site-detail.component";
import {SitePopupComponent} from "./site-dialog.component";
import {SiteDeletePopupComponent} from "./site-delete-dialog.component";

@Injectable()
export class SiteResolvePagingParams implements Resolve<any> {

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

export const siteRoute: Routes = [
	{
		path: 'site',
		component: SiteComponent,
		resolve: {
			'pagingParams': SiteResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.site.home.title'
		}
	}, {
		path: 'site/:id',
		component: SiteDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.site.home.title'
		}
	}
];

export const sitePopupRoute: Routes = [
	{
		path: 'site-new',
		component: SitePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.site.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'site/:id/edit',
		component: SitePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.site.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'site/:id/delete',
		component: SiteDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.site.home.title'
		},
		outlet: 'popup'
	}
];
