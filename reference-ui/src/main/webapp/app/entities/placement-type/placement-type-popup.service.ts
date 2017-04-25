import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {PlacementType} from "./placement-type.model";
import {PlacementTypeService} from "./placement-type.service";
@Injectable()
export class PlacementTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private placementTypeService: PlacementTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.placementTypeService.find(id).subscribe(placementType => {
				this.placementTypeModalRef(component, placementType);
			});
		} else {
			return this.placementTypeModalRef(component, new PlacementType());
		}
	}

	placementTypeModalRef(component: Component, placementType: PlacementType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.placementType = placementType;
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
