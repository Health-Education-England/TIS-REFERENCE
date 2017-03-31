import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {CollegeComponent} from "./college.component";
import {CollegeDetailComponent} from "./college-detail.component";
import {CollegePopupComponent} from "./college-dialog.component";
import {CollegeDeletePopupComponent} from "./college-delete-dialog.component";

@Injectable()
export class CollegeResolvePagingParams implements Resolve<any> {

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

export const collegeRoute: Routes = [
	{
		path: 'college',
		component: CollegeComponent,
		resolve: {
			'pagingParams': CollegeResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.college.home.title'
		}
	}, {
		path: 'college/:id',
		component: CollegeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.college.home.title'
		}
	}
];

export const collegePopupRoute: Routes = [
	{
		path: 'college-new',
		component: CollegePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.college.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'college/:id/edit',
		component: CollegePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.college.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'college/:id/delete',
		component: CollegeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.college.home.title'
		},
		outlet: 'popup'
	}
];
