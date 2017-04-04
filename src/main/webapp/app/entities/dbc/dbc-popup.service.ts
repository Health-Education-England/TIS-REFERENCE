import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {DBC} from "./dbc.model";
import {DBCService} from "./dbc.service";
@Injectable()
export class DBCPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private dBCService: DBCService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.dBCService.find(id).subscribe(dBC => {
				this.dBCModalRef(component, dBC);
			});
		} else {
			return this.dBCModalRef(component, new DBC());
		}
	}

	dBCModalRef(component: Component, dBC: DBC): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.dBC = dBC;
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
