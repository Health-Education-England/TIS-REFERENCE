import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Site} from "./site.model";
import {SiteService} from "./site.service";
@Injectable()
export class SitePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private siteService: SiteService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.siteService.find(id).subscribe(site => {
				this.siteModalRef(component, site);
			});
		} else {
			return this.siteModalRef(component, new Site());
		}
	}

	siteModalRef(component: Component, site: Site): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.site = site;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
