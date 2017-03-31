import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EthnicOrigin} from "./ethnic-origin.model";
import {EthnicOriginService} from "./ethnic-origin.service";
@Injectable()
export class EthnicOriginPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private ethnicOriginService: EthnicOriginService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.ethnicOriginService.find(id).subscribe(ethnicOrigin => {
				this.ethnicOriginModalRef(component, ethnicOrigin);
			});
		} else {
			return this.ethnicOriginModalRef(component, new EthnicOrigin());
		}
	}

	ethnicOriginModalRef(component: Component, ethnicOrigin: EthnicOrigin): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.ethnicOrigin = ethnicOrigin;
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
